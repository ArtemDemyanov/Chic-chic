package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Application;
import com.example.demo.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    ApplicationRepository applicationRepository;

    public ApplicationService() {
        super();
        // TODO Auto-generated constructor stub
    }


    public List<Application> getApplicationsUsluga(Long id) {
        return (List<Application>) applicationRepository.findByUslugaId(id);
    }
    public List<Application> getApplicationsUser(Long id) {
        return (List<Application>) applicationRepository.findByUserId(id);
    }
    public Optional<Application> findByID(Long id) {
        return applicationRepository.findById(id);
    }
    public void addApplication(Application newApplication) {
        applicationRepository.save(newApplication);
    }
    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));
        applicationRepository.delete(application);
    }
}
