package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findItemRequestsByRequestorId(Long userId);

    @Query(value = "SELECT r " +
            "FROM ItemRequest r " +
            "WHERE r.requestor.id <> :userId")
    List<ItemRequest> findRequests(Long userId, Pageable pageable);
}
