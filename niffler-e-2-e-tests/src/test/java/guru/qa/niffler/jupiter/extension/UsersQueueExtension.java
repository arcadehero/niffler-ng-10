package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username, String password, String friend, String income, String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("cat", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("duck", "12345", "bee", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("dog", "12345", null, "bee", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("bee", "12345", null, null, "dog"));
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Map<UserType.Type, StaticUser> usersMap = new HashMap<>();
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(parameter -> {
                    UserType userType = parameter.getAnnotation(UserType.class);
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = switch (userType.value()) {
                            case EMPTY -> ofNullable(EMPTY_USERS.poll());
                            case WITH_FRIEND -> ofNullable(WITH_FRIEND_USERS.poll());
                            case WITH_INCOME_REQUEST -> ofNullable(WITH_INCOME_REQUEST_USERS.poll());
                            case WITH_OUTCOME_REQUEST -> ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
                        };
                    }
                    user.ifPresentOrElse(
                            u -> {
                                usersMap.put(userType.value(), u);
                            }, () -> {
                                throw new IllegalStateException("Didn't find any user");
                            }
                    );
                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );
                });
        context.getStore(NAMESPACE).put(context.getUniqueId(), usersMap);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType.Type, StaticUser> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );
        if (map != null) {
            for (Map.Entry<UserType.Type, StaticUser> entry : map.entrySet()) {
                getQueueByType(entry.getKey()).add(entry.getValue());
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        UserType userType = parameterContext.findAnnotation(UserType.class).orElseThrow();
        Map<UserType.Type, StaticUser> users = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        if (users == null) {
            throw new ParameterResolutionException("Map contains no users");
        }
        StaticUser staticUser = users.get(userType.value());
        if (staticUser == null) {
            throw new ParameterResolutionException("No user found for annotation: " + userType.value());
        }
        return staticUser;
    }

    private Queue<StaticUser> getQueueByType(UserType.Type type) {
        return switch (type) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
        };
    }
}
