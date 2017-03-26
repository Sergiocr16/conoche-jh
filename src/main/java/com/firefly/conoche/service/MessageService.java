package com.firefly.conoche.service;

import com.firefly.conoche.domain.Message;
import com.firefly.conoche.repository.MessageRepository;
import com.firefly.conoche.service.dto.MessageDTO;
import com.firefly.conoche.service.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Message.
 */
@Service
@Transactional
public class MessageService {

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    /**
     * Save a message.
     *
     * @param messageDTO the entity to save
     * @return the persisted entity
     */
    public MessageDTO save(MessageDTO messageDTO) {
        log.debug("Request to save Message : {}", messageDTO);
        Message message = messageMapper.messageDTOToMessage(messageDTO);
        message = messageRepository.save(message);
        MessageDTO result = messageMapper.messageToMessageDTO(message);
        return result;
    }

    /**
     *  Get all the messages.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Messages");
        Page<Message> result = messageRepository.findAll(pageable);
        return result.map(message -> messageMapper.messageToMessageDTO(message));
    }

    @Transactional(readOnly = true)
    public Page<MessageDTO> findByEventId(Pageable pageable,Long eventId) {
        log.debug("Request to get all Messages");
        Page<Message> result = messageRepository.findByEventId(pageable,eventId);
        return result.map(message -> messageMapper.messageToMessageDTO(message));
    }

    /**
     *  Get one message by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MessageDTO findOne(Long id) {
        log.debug("Request to get Message : {}", id);
        Message message = messageRepository.findOne(id);
        MessageDTO messageDTO = messageMapper.messageToMessageDTO(message);
        return messageDTO;
    }

    /**
     *  Delete the  message by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Message : {}", id);
        messageRepository.delete(id);
    }
}
