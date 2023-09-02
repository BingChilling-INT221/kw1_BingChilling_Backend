package sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation;

import com.jayway.jsonpath.Criteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.UniqueKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sit.int221.sas.sit_announcement_system_backend.entity.User;

import sit.int221.sas.sit_announcement_system_backend.repository.UserRepo.CustomUserRepository;
import sit.int221.sas.sit_announcement_system_backend.repository.UserRepo.UserRepository;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class CheckUniqueValidator implements ConstraintValidator<CheckUnique, String> {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void initialize(CheckUnique constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
            User user = userRepository.findUsersByUsernameAndNameAndEmail(value);
            return user == null;

    }


}


//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//
//    private String[] columnNames;
//
//    public void initialize(CheckUnique constraintAnnotation) {
//
//    }
//
//    @Override
//    public boolean isValid(Serializable target, ConstraintValidatorContext context) {
//
//    }








//    Class<?> entityClass = target.getClass();
//
//    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//
//    CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
//
//    Root<?> root = criteriaQuery.from(entityClass);
//
//    List<Predicate> predicates = new ArrayList<Predicate>(columnNames.length);
//
//        try {
//                for(int i=0; i<columnNames.length; i++) {
//        String propertyName = columnNames[i];
//        PropertyDescriptor desc = new PropertyDescriptor(propertyName, entityClass);
//        Method readMethod = desc.getReadMethod();
//        Object propertyValue = readMethod.invoke(target);
//        Predicate predicate = criteriaBuilder.equal(root.get(propertyName), propertyValue);
//        predicates.add(predicate);
//        }
//        } catch (Exception e) {
//        e.printStackTrace();
//        }
//
//        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
//
//        TypedQuery<Object> typedQuery = entityManager.createQuery(criteriaQuery);
//
//        List<Object> resultSet = typedQuery.getResultList();
//
//        resultSet.forEach(x-> System.out.println(x));
//        return resultSet.size() == 0;
////        return false ;