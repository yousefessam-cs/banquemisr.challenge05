package com.banquemisr.challenge05.specification;

import com.banquemisr.challenge05.enums.TaskPriority;
import com.banquemisr.challenge05.enums.TaskStatus;
import com.banquemisr.challenge05.model.Task;
import com.banquemisr.challenge05.specification.criteria.TaskSearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class TaskSpecification {

    private static final Logger logger = Logger.getLogger(TaskSpecification.class.getName());

    public static Specification<Task> buildSearchCriteria(TaskSearchCriteria searchCriteria) {
        if (searchCriteria == null) {
            throw new IllegalArgumentException("TaskSearchCriteria must not be null");
        }

        logger.info("Building Specification for TaskSearchCriteria: " + searchCriteria);

        return Specification.where(validateAndLog(titleContains(searchCriteria.getTitle()), "Title Filter"))
                .and(validateAndLog(descriptionContains(searchCriteria.getDescription()), "Description Filter"))
                .and(validateAndLog(statusIs(searchCriteria.getStatus()), "Status Filter"))
                .and(validateAndLog(priorityIs(searchCriteria.getPriority()), "Priority Filter"))
                .and(validateAndLog(dueDateFrom(searchCriteria.getDueDateFrom()), "Due Date From Filter"))
                .and(validateAndLog(dueDateTo(searchCriteria.getDueDateTo()), "Due Date To Filter"))
                .and(validateAndLog(assignedUserIdIs(searchCriteria.getAssignedUserId()), "Assigned User ID Filter"));
    }

    private static Specification<Task> titleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (isNullOrEmpty(title)) {
                logger.info("Title filter not applied (null or empty).");
                return null;
            }
            logger.info("Applying title filter: " + title);
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        };
    }

    private static Specification<Task> descriptionContains(String description) {
        return (root, query, criteriaBuilder) -> {
            if (isNullOrEmpty(description)) {
                logger.info("Description filter not applied (null or empty).");
                return null;
            }
            logger.info("Applying description filter: " + description);
            return criteriaBuilder.like(root.get("description"), "%" + description + "%");
        };
    }

    private static Specification<Task> statusIs(TaskStatus taskStatus) {
        return (root, query, criteriaBuilder) -> {
            if (taskStatus == null) {
                logger.info("Status filter not applied (null).");
                return null;
            }
            logger.info("Applying status filter: " + taskStatus);
            return criteriaBuilder.equal(root.get("status"), taskStatus);
        };
    }

    private static Specification<Task> priorityIs(TaskPriority priority) {
        return (root, query, criteriaBuilder) -> {
            if (priority == null) {
                logger.info("Priority filter not applied (null).");
                return null;
            }
            logger.info("Applying priority filter: " + priority);
            return criteriaBuilder.equal(root.get("priority"), priority);
        };
    }

    private static Specification<Task> dueDateFrom(LocalDateTime dueDateFrom) {
        return (root, query, criteriaBuilder) -> {
            if (dueDateFrom == null) {
                logger.info("Due Date From filter not applied (null).");
                return null;
            }
            logger.info("Applying Due Date From filter: " + dueDateFrom);
            return criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), dueDateFrom);
        };
    }

    private static Specification<Task> dueDateTo(LocalDateTime dueDateTo) {
        return (root, query, criteriaBuilder) -> {
            if (dueDateTo == null) {
                logger.info("Due Date To filter not applied (null).");
                return null;
            }
            logger.info("Applying Due Date To filter: " + dueDateTo);
            return criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), dueDateTo);
        };
    }

    private static Specification<Task> assignedUserIdIs(Long assignedUserId) {
        return (root, query, criteriaBuilder) -> {
            if (assignedUserId == null) {
                logger.info("Assigned User ID filter not applied (null).");
                return null;
            }
            logger.info("Applying Assigned User ID filter: " + assignedUserId);
            return criteriaBuilder.equal(root.join("assignedTo", JoinType.LEFT).get("id"), assignedUserId);
        };
    }

    private static Specification<Task> validateAndLog(Specification<Task> specification, String filterName) {
        if (specification == null) {
            logger.info(filterName + " not applied.");
        } else {
            logger.info(filterName + " applied successfully.");
        }
        return specification;
    }

    private static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

}