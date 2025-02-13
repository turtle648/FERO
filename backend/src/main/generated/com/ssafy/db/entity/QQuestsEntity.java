package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestsEntity is a Querydsl query type for QuestsEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQuestsEntity extends EntityPathBase<QuestsEntity> {

    private static final long serialVersionUID = -145794090L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestsEntity questsEntity = new QQuestsEntity("questsEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QExerciseStatsRatio exercise;

    public final NumberPath<Integer> exerciseCnt = createNumber("exerciseCnt", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isCompleted = createBoolean("isCompleted");

    public final StringPath message = createString("message");

    public final DatePath<java.time.LocalDate> questDate = createDate("questDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> questTime = createTime("questTime", java.time.LocalTime.class);

    public final NumberPath<Integer> realCnt = createNumber("realCnt", Integer.class);

    public final QUserCharacter userCharacter;

    public QQuestsEntity(String variable) {
        this(QuestsEntity.class, forVariable(variable), INITS);
    }

    public QQuestsEntity(Path<? extends QuestsEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestsEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestsEntity(PathMetadata metadata, PathInits inits) {
        this(QuestsEntity.class, metadata, inits);
    }

    public QQuestsEntity(Class<? extends QuestsEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.exercise = inits.isInitialized("exercise") ? new QExerciseStatsRatio(forProperty("exercise")) : null;
        this.userCharacter = inits.isInitialized("userCharacter") ? new QUserCharacter(forProperty("userCharacter"), inits.get("userCharacter")) : null;
    }

}

