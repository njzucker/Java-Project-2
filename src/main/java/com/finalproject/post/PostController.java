package com.finalproject.post;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.models.Post;
import com.finalproject.user.UserRepository;

//1.
@RestController
@RequestMapping("/api/post")
public class PostController {

    //2.
    @Autowired
    PostRepository postRepo;
    @Autowired
    UserRepository userRepo;


    //3.
    @GetMapping("")
    public List<Post> getMessages() {
        List<Post> foundMessages = postRepo.findAll();    
        return foundMessages;
    }

//    gets one by id
    @GetMapping("/{id}")
    public ResponseEntity<Post> getMessage(@PathVariable("id") Long id) {
    	Post foundMessage = postRepo.findById(id).orElse(null);

        if(foundMessage == null) {
            return ResponseEntity.notFound().header("Message","Nothing found with that id").build();
        }
        return ResponseEntity.ok(foundMessage);
    }

//    creates post
    @PostMapping("")
    public ResponseEntity<Post> postMessage(@RequestBody Post post, Principal myPrincipal) {
    	post.setTimestamp(LocalDateTime.now());
    	post.setAuthorId(userRepo.findByUsername(myPrincipal.getName()).getId());
//    	System.out.println(userRepo.findByUsername(myPrincipal.getName()).getId());       
//    	System.out.println(userRepo.findByUsername(myPrincipal.getName()).getUsername());       
    	// saving to DB using instance of the repo interface
    	Post createdMessage = postRepo.save(post);

        // RespEntity crafts response to include correct status codes.
        return ResponseEntity.ok(createdMessage);
    }
    
//    getting all posts that belong to one user
    @GetMapping("/user/{id}")
    public List<Post> getMessagesNyUser(@PathVariable(value="id") Long id) {
        List<Post> foundMessages = postRepo.findAll();
        List<Post> userMessages = new ArrayList<Post>();
        for(Post message : foundMessages) {
        	if (message.getAuthorId() == id) {
        		userMessages.add(message);
        	
        	}
        }
        return userMessages;
    }

//    deleting posts by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deleteMessage(@PathVariable(value="id") Long id) {
    	Post foundMessage = postRepo.findById(id).orElse(null);

        if(foundMessage == null) {
            return ResponseEntity.notFound().header("Message","Nothing found with that id").build();
        }else {
        	postRepo.delete(foundMessage);
        }
        return ResponseEntity.ok().build();
    }
}
