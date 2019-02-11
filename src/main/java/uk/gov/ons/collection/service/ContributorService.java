package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ContributorEntity;

@Service
public class ContributorService {

    @Autowired
    ContributorProxy contributorProxy;

    public Iterable<ContributorEntity> generalSearch(String parameters){
        return contributorProxy.searchContributor(parameters);
    }
}
