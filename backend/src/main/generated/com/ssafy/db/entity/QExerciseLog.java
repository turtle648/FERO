package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExerciseLog is a Querydsl query type for ExerciseLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QExerciseLog extends EntityPathBase<ExerciseLog> {

    private static final long serialVersionUID = 1816473930L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExerciseLog exerciseLog = new QExerciseLog("exerciseLog");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Integer> exerciseCount = createNumber("exerciseCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> exerciseDate = createDateTime("exerciseDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> exerciseDuration = createNumber("exerciseDuration", Integer.class);

    public final QExerciseStatsRatio exerciseStatsRatio;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QUser user;

    public QExerciseLog(String variable) {
        this(ExerciseLog.class, forVariable(variable), INITS);
    }

    public QExerciseLog(Path<? extends ExerciseLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExerciseLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExerciseLog(PathMetadata metadata, PathInits inits) {
        this(ExerciseLog.class, metadata, inits);
    }

    public QExerciseLog(Class<? extends ExerciseLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.exerciseStatsRatio = inits.isInitialized("exerciseStatsRatio") ? new QExerciseStatsRatio(forProperty("exerciseStatsRatio")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

