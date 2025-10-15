package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendApiClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CreateCategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(CreateCategoryExtension.class);
    private final SpendApiClient spendClient = new SpendApiClient();
    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                        context.getRequiredTestMethod(), Category.class)
                .ifPresent(
                        anno -> {
                            CategoryJson createdCategory = spendClient.createCategory(
                                    new CategoryJson(
                                            null,
                                            faker.name().name(),
                                            anno.username(),
                                            false
                                    )
                            );
                            if (anno.archived()) {
                                CategoryJson archivedCategory = new CategoryJson(
                                        createdCategory.id(),
                                        createdCategory.name(),
                                        createdCategory.username(),
                                        true
                                );
                                createdCategory = spendClient.updateCategory(archivedCategory);
                            }
                            context.getStore(NAMESPACE).put(
                                    context.getUniqueId(),
                                    createdCategory
                            );
                        }
                );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (!category.archived()) {
            CategoryJson archivedCategory = new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            );
            spendClient.updateCategory(archivedCategory);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);

    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
