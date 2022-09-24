package com.datn.coworkingspace.service;


import com.datn.coworkingspace.dto.CommentDTO;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.Comment;
import com.datn.coworkingspace.entity.Space;
import com.datn.coworkingspace.entity.User;
import com.datn.coworkingspace.exception.ResourceNotFoundException;
import com.datn.coworkingspace.repository.CommentRepository;
import com.datn.coworkingspace.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentService implements ICommentService {

    @Autowired
    private ISpaceService spaceService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IUserService customerService;

    @Autowired
    private SpaceRepository spaceRepository;


    @Override
    public MessageResponse createComment(CommentDTO theCommentDTO) {

        Comment theComment = new Comment();

        Space space = spaceService.findById(theCommentDTO.getSpaceId());
        User user = customerService.findByIdCustomer(theCommentDTO.getUserId());

        theComment.setContent(theCommentDTO.getContent());
        theComment.setRate(theCommentDTO.getRate());
        theComment.setUser(user);
        theComment.setSpace(space);
        theComment.setCreatedDate(new Date());
        theComment.setCreatedBy(user.getName());

        commentRepository.save((theComment));
        updateRatingAverage(theCommentDTO, space);
        return new MessageResponse("Create comment successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public Page<Comment> findAllPageAndSortComment(Pageable pagingSort) {
        return commentRepository.findAll(pagingSort);
    }

    @Override
    public Page<Comment> findBySpaceIdPageAndSort(Long spaceId, Pageable pagingSort) {
        Space space = spaceService.findById(spaceId);
        Page<Comment> commentPage = commentRepository.findBySpaceId(spaceId, pagingSort);

        if (space == null) {
            throw new ResourceNotFoundException("Not found space with ID= " + spaceId);
        } else {

            try {
                for(Comment comment : commentPage.getContent()) {
                    comment.setSpaceIds(comment.getSpace().getId());
                    comment.setUserIds(comment.getUser().getId());
                }
                }
            catch (Exception e) {
                e.printStackTrace();
            }

            return  commentPage;
        }
    }

    private void updateRatingAverage(CommentDTO theCommentDTO, Space space) {
        long sum = 0 ;
        List<Comment> comments = commentRepository.findBySpaceId(theCommentDTO.getSpaceId());
        if(comments.size() != 0) {
            for (Comment comment: comments) {
                sum += comment.getRate();
            }
            final DecimalFormat df = new DecimalFormat("0.00");
            float average =  (float) sum/ (float) comments.size();
            BigDecimal convertAverage = new BigDecimal (df.format(average));
            space.setRatingAverage(convertAverage);

            spaceRepository.save(space);
        }
    }
}