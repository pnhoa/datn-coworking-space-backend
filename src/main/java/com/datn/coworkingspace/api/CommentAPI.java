package com.datn.coworkingspace.api;

import com.datn.coworkingspace.dto.CommentDTO;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.dto.RoleDTO;
import com.datn.coworkingspace.entity.Comment;
import com.datn.coworkingspace.entity.Role;
import com.datn.coworkingspace.service.ICommentService;
import com.datn.coworkingspace.service.IRoleService;
import com.datn.coworkingspace.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class CommentAPI {
    @Autowired
    private ICommentService commentService;

    @GetMapping("")
    public ResponseEntity<?> findAll(@RequestParam(name = "spaceId", required = false) Long spaceId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int limit,
                                     @RequestParam(defaultValue = "id,ASC") String[] sort) {

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<Comment> commentPage;

            if (spaceId == null) {
                commentPage = commentService.findAllPageAndSortComment(pagingSort);
            } else {
                commentPage = commentService.findBySpaceIdPageAndSort(spaceId, pagingSort);
            }

            return new ResponseEntity<>(commentPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentDTO theCommentDTO, BindingResult theBindingResult) {

        if (theBindingResult.hasErrors()) {
            return new ResponseEntity<>(new MessageResponse("Invalid value for create review", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = commentService.createComment(theCommentDTO);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

}
