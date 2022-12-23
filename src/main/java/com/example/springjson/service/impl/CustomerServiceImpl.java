package com.example.springjson.service.impl;

import com.example.springjson.model.CustomerRequest;
import com.example.springjson.model.CustomerResponse;
import com.example.springjson.service.CustomerService;
import com.example.springjson.entity.CustomerEntity;
import com.example.springjson.model.CustomerModel;
import com.example.springjson.repository.AddressRepo;
import com.example.springjson.repository.CustomerRepo;
import com.example.springjson.repository.SchoolRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepo repo;

    @Autowired
    public CustomerServiceImpl(CustomerRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<CustomerModel> getAll() {
        return this.repo.findAll().stream().map(CustomerModel::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerModel> getById(Long id) {
        if(id == 0) {
            return Optional.empty();
        }
        Optional<CustomerEntity> result = this.repo.findById(id);
        return result.map(CustomerModel::new);
    }

    @Override
    public CustomerResponse saveAll(CustomerRequest request) {
        if(request.getCustomers().isEmpty()) {
            return new CustomerResponse();
        }
        CustomerResponse response = new CustomerResponse();
        int countSuccess = 0;
        int countFailed = 0;
        List<CustomerModel> customerModels = new ArrayList<>();
        for (CustomerModel model: request.getCustomers()){
            // panggil method save
            Optional<CustomerModel> customerModel = this.save(model);
            // check datanya
            if(customerModel.isPresent()){
                customerModels.add(model);
                countSuccess++;
            }else {
                countFailed++;
            }
        }
        // return  new CustomerResponse(customerModels, countSuccess, countFailed);

        response.setData(customerModels);
        response.setSuccessSave(countSuccess);
        response.setFailedSave(countFailed);
        return response;
    }

    @Override
    public Optional<CustomerModel> save(CustomerModel model) {
        if(model == null) {
            return Optional.empty();
        }
        CustomerEntity entity = new CustomerEntity(model);
        try {
            this.repo.save(entity);
            return Optional.of(new CustomerModel(entity));
        }catch (Exception e){
            log.error("Customer save is failed, error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<CustomerModel> update(Long id, CustomerModel model) {
        if(id == 0) {
            return Optional.empty();
        }

        CustomerEntity result = this.repo.findById(id).orElse(null);
        if(result == null){
            return Optional.empty();
        }

        // copy property
        BeanUtils.copyProperties(model, result);
        try {
            this.repo.save(result);
            return Optional.of(new CustomerModel(result));
        }catch (Exception e){
            log.error("Customer update is failed, error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<CustomerModel> delete(Long id) {
        if(id == 0) {
            return Optional.empty();
        }

        CustomerEntity result = this.repo.findById(id).orElse(null);
        if(result == null){
            return Optional.empty();
        }

        try {
            this.repo.delete(result);
            return Optional.of(new CustomerModel(result));
        }catch (Exception e){
            log.error("Customer delete is failed, error: {}", e.getMessage());
            return Optional.empty();
        }
    }
}