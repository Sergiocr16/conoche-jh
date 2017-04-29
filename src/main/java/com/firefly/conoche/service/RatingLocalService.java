package com.firefly.conoche.service;

import com.firefly.conoche.domain.Local;
import com.firefly.conoche.domain.RatingLocal;
import com.firefly.conoche.repository.LocalRepository;
import com.firefly.conoche.repository.RatingLocalRepository;
import com.firefly.conoche.service.dto.RatingLocalDTO;
import com.firefly.conoche.service.mapper.RatingLocalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing RatingLocal.
 */
@Service
@Transactional
public class RatingLocalService {

    private final Logger log = LoggerFactory.getLogger(RatingLocalService.class);

    private final RatingLocalRepository ratingLocalRepository;

    private final RatingLocalMapper ratingLocalMapper;

    private final LocalRepository localRepository;

    public RatingLocalService(RatingLocalRepository ratingLocalRepository, RatingLocalMapper ratingLocalMapper,LocalRepository localRepository) {
        this.ratingLocalRepository = ratingLocalRepository;
        this.ratingLocalMapper = ratingLocalMapper;
        this.localRepository = localRepository;
    }

    /**
     * Save a ratingLocal.
     *
     * @param ratingLocalDTO the entity to save
     * @return the persisted entity
     */
    public RatingLocalDTO save(RatingLocalDTO ratingLocalDTO) {
        log.debug("Request to save RatingLocal : {}", ratingLocalDTO);
        RatingLocal ratingLocal = ratingLocalMapper.ratingLocalDTOToRatingLocal(ratingLocalDTO);
        ratingLocal = ratingLocalRepository.save(ratingLocal);
         Local local =  localRepository.getOne(ratingLocal.getLocal().getId());
         local.setRating(calculateRating(local.getId()));
         localRepository.save(local);
        RatingLocalDTO result = ratingLocalMapper.ratingLocalToRatingLocalDTO(ratingLocal);
        return result;
    }

    /**
     *  Get all the ratingLocals.
     *
     *  @param localId the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RatingLocalDTO> findAll(Long localId) {
        log.debug("Request to get all RatingLocals");
        List<RatingLocal> result = ratingLocalRepository.findByLocalId(localId);
        return new PageImpl<RatingLocal>(result).map(ratingLocal -> ratingLocalMapper.ratingLocalToRatingLocalDTO(ratingLocal));
    }

    /**
     *  Get one ratingLocal by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RatingLocalDTO findOne(Long id) {
        log.debug("Request to get RatingLocal : {}", id);
        RatingLocal ratingLocal = ratingLocalRepository.findOne(id);
        RatingLocalDTO ratingLocalDTO = ratingLocalMapper.ratingLocalToRatingLocalDTO(ratingLocal);
        return ratingLocalDTO;
    }
    /**
     *  Get one ratingLocal by login and local id.
     *  author Sergio Castor
     *  @param localId the id of the local
     *  @param userLogin the login of the user
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RatingLocalDTO findOneByLoginAndLocalId(String userLogin,Long localId) {
        log.debug("Request to get RatingLocal by local and user : {}", localId,userLogin);
        RatingLocal ratingLocal = ratingLocalRepository.findByUserLoginAndLocalId(userLogin,localId);
        RatingLocalDTO ratingLocalDTO = ratingLocalMapper.ratingLocalToRatingLocalDTO(ratingLocal);
        return ratingLocalDTO;
    }



    /**
     *  Delete the  ratingLocal by id.
     *  author Sergio Castor
     *  @param id the id of the entity
     *  @return the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RatingLocal : {}", id);
        ratingLocalRepository.delete(id);
    }
    /**
     *  Calculate rating in base of the average of all ratings.
     *  author Sergio Castor
     *  @param localId the id of the local
     *  @return the rating of the local
     */
    @Transactional(readOnly = true)
    public double calculateRating(Long localId) {
        log.debug("Request to get all local rating");
        List<RatingLocal> ratings = ratingLocalRepository.findByLocalId(localId);
        int sum = 0;
        for (int i=0;i<ratings.size();i++) {
            sum += ratings.get(i).getRating();
        }
        return (sum/(ratings.size()));
    }
}
