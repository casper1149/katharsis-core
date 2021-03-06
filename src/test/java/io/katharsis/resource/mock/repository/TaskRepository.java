package io.katharsis.resource.mock.repository;

import io.katharsis.queryParams.RequestParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.katharsis.resource.mock.models.Task;

import java.util.HashMap;
import java.util.Map;

public class TaskRepository implements ResourceRepository<Task, Long> {

    // Used ThreadLocal in case of switching to TestNG and using concurrent tests
    private static final ThreadLocal<Map<Long, Task>> THREAD_LOCAL_REPOSITORY = new ThreadLocal<Map<Long, Task>>() {
        @Override
        protected Map<Long, Task> initialValue() {
            return new HashMap<>();
        }
    };

    @Override
    public <S extends Task> S save(S entity) {
        entity.setId((long) (THREAD_LOCAL_REPOSITORY.get().size() + 1));
        THREAD_LOCAL_REPOSITORY.get().put(entity.getId(), entity);

        return entity;
    }

    @Override
    public Task findOne(Long aLong, RequestParams requestParams) {
        Task task = THREAD_LOCAL_REPOSITORY.get().get(aLong);
        if (task == null) {
            throw new ResourceNotFoundException("");
        }
        return task;
    }

    @Override
    public Iterable<Task> findAll(RequestParams requestParams) {
        return THREAD_LOCAL_REPOSITORY.get().values();
    }


    @Override
    public Iterable<Task> findAll(Iterable<Long> longs, RequestParams requestParams) {
        return null;
    }

    @Override
    public void delete(Long aLong) {
        THREAD_LOCAL_REPOSITORY.get().remove(aLong);
    }
}
