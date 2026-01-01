package guru.qa.niffler.debug;

import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.Optional;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();

        SpendDaoJdbc spendDaoJdbc = new SpendDaoJdbc();

        Optional<SpendEntity> spendById = spendDaoJdbc.findSpendById(UUID.fromString("8528d951-5c34-4b3b-9097-8d3b13baec7a"));
        System.out.println(spendById);


//        List<CategoryEntity> allByUserName = categoryDaoJdbc.findAllByUserName("arcadehero");

//        allByUserName.size();
//        allByUserName.forEach(System.out::println);
    }


}

