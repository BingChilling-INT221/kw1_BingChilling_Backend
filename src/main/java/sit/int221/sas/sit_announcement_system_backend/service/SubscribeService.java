package sit.int221.sas.sit_announcement_system_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int221.sas.sit_announcement_system_backend.entity.Subscribe;
import sit.int221.sas.sit_announcement_system_backend.repository.SubscribeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscribeService {
    @Autowired
    SubscribeRepository subscribeRepository ;

    public List<Subscribe> getSubscribes (){
        return  subscribeRepository.findAll();
    }

    public List<Subscribe> AddSubScribe(String email,List<Integer> categorys){
       List<Subscribe> subscribesOfEmail = subscribeRepository.findByEmail(email);
       List<Integer> tempcategory = new ArrayList<>();
        categorys.forEach((catagory)->{
            tempcategory.add(catagory) ;
            subscribesOfEmail.forEach((subscribe)->{
                    if(subscribe.getCategory_id().equals(catagory)){
                            tempcategory.remove(tempcategory.size()-1);
                    }
                });
        });
        tempcategory.forEach((x)-> {
            subscribeRepository.saveAndFlush(new Subscribe(email,x)) ;
        });
        return subscribeRepository.findByEmail(email);
    }

}
