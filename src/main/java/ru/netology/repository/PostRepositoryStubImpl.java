package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
    private final AtomicLong count;
    private final Map<Long, Post> repository;

    public PostRepositoryStubImpl() {
        long initialCount = 1;
        this.count = new AtomicLong(initialCount);
        this.repository = new ConcurrentHashMap<>();
    }


    public List<Post> all() {
        return repository.values().stream()
                .filter(Objects::nonNull)
                .filter(post -> !post.isRemoved())
                .collect(Collectors.toList());
    }


    public Post getById(long id) {
        if (repository.containsKey(id) && !repository.get(id).isRemoved()) {
           return repository.get(id);

        } else {
            throw new NotFoundException("Post not found in \"getById\" method");
        }
    }


    public Post save(Post post) {
        long postId = post.getId();

        if (postId == 0) {
            post.setId(count.getAndIncrement());
            repository.put(post.getId(), post);
            return post;

        } else if (repository.containsKey(postId)
                && !repository.get(postId).isRemoved()) {
            repository.put(postId, post);
            return post;

        } else {
            throw new NotFoundException("Post not found in \"save\" method");
        }
    }

    public void removeById(long id) {
        if (repository.containsKey(id) && !repository.get(id).isRemoved()) {
            repository.get(id).setRemoved(true);

        } else {
            throw new NotFoundException("Post not found in \"removeById\" method");
        }
    }
}