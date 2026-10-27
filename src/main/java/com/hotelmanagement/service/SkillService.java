package com.hotelmanagement.service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotelmanagement.model.Skill;
import com.hotelmanagement.repository.SkillRepository;

@Service 
public class SkillService {
    public static final String NOT_FOUND = "The skill with that id does not exist";

@Autowired 
private SkillRepository skillRepository;

public String save(Skill skill) {
    skill.setId(null);
    skillRepository.save(skill);
    return "Skill saved successfully";

}
public List<Skill> getAll(){
    return skillRepository.findAll();
}

public Optional<Skill> getById(UUID id){

    return skillRepository.findById(id);
}
public String update(UUID id,Skill input){
    Optional<Skill> found = skillRepository.findById(id);
    if (found.isEmpty()){
        return NOT_FOUND;
    }
Skill skill = found.get();
skill.setName(input.getName());
skillRepository.save(skill);
return "Skill update successfully";

}

public String delete(UUID id){

    Optional<Skill> found = skillRepository.findById(id);

if (found.isEmpty()){

    return NOT_FOUND;
}
skillRepository.delete(found.get());
return "Skill deleted successfully";


    }

    //C3


    public List<Skill> getUnused(){
        return skillRepository.findUnused();
    }
}













