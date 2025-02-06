package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserTutorialProgress is a Querydsl query type for UserTutorialProgress
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserTutorialProgress extends EntityPathBase<UserTutorialProgress> {

    private static final long serialVersionUID = 655807576L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserTutorialProgress userTutorialProgress = new QUserTutorialProgress("userTutorialProgress");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> completedAt = createDateTime("completedAt", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isCompleted = createBoolean("isCompleted");

    public final QTutorialType tutorialType;

    public final QUser user;

    public QUserTutorialProgress(String variable) {
        this(UserTutorialProgress.class, forVariable(variable), INITS);
    }

    public QUserTutorialProgress(Path<? extends UserTutorialProgress> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserTutorialProgress(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserTutorialProgress(PathMetadata metadata, PathInits inits) {
        this(UserTutorialProgress.class, metadata, inits);
    }

    public QUserTutorialProgress(Class<? extends UserTutorialProgress> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tutorialType = inits.isInitialized("tutorialType") ? new QTutorialType(forProperty("tutorialType")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

