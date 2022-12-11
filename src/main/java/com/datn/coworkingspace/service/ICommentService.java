package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.CommentDTO;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICommentService {


    MessageResponse createComment(CommentDTO theCommentDTO);

    Page<Comment> findAllPageAndSortComment(Pageable pagingSort);

    Page<Comment> findBySpaceIdPageAndSort(Long spaceId, Pageable pagingSort);

}
