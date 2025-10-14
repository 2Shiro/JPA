package com.andgivemarketing.problemmaker;

import com.andgivemarketing.problemmaker.dto.UnitDTO;
import com.andgivemarketing.problemmaker.entity.ProblemEntity;
import com.andgivemarketing.problemmaker.entity.UnitEntity;
import com.andgivemarketing.problemmaker.repository.UnitRepository;
import com.andgivemarketing.problemmaker.service.ProblemService;
import com.andgivemarketing.problemmaker.service.UnitService;
import com.andgivemarketing.problemmaker.utils.GsonUtils;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

@SpringBootApplication
public class ProblemMakerApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(ProblemMakerApplication.class, args);



        UnitRepository unitRepository = run.getBean(UnitRepository.class);

        ProblemService problemService = run.getBean(ProblemService.class);
        UnitService unitService = run.getBean(UnitService.class);


//        Optional<UnitEntity> optionalUnitEntity = unitService.findById(1L);
//        if (optionalUnitEntity.isPresent()){
//            UnitEntity unitEntity = optionalUnitEntity.get();
//            unitService.delete(unitEntity);
//            System.out.println(GsonUtils.gson.toJson(unitEntity));
//        }
    }

}
