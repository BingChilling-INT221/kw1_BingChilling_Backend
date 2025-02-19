package sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerMapping;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.repository.UserRepo.UserRepository;

import java.util.Map;

@Getter
@Setter
@Transactional
public class CheckUniqueValidator implements ConstraintValidator<CheckUnique, String> {
    @Autowired
    private HttpServletRequest request;
    //    private  Integer idError;
//    private List<String> userExists ;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(CheckUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value != null) {
            Map map = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Object id = map.get("id");
            User result;
            if (id != null) {
                result = userRepository.findUsersByUsernameAndNameAndEmail(value.trim(), Integer.parseInt(id.toString())).orElse(null);
            } else {
                result = userRepository.findUsersByUsernameAndNameAndEmail(value.trim(), null).orElse(null);
            }
            return result == null;
        } else {
            return true;
        }


        //            if(value==null){return true ;}
//            User user = userRepository.findUsersByUsernameAndNameAndEmail(value).orElse(null);
//            if(user != null){
//                Map map = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//                Object id = map.get("id");
//                if(id != null){
//                    User userExist = userRepository.findById(Integer.parseInt(id.toString())).get();
//                    return user.getId().equals(userExist.getId());
//                }
//                else {return false ;}
//            }
//        return true;


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