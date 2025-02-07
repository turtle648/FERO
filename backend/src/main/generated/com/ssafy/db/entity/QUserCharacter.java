package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserCharacter is a Querydsl query type for UserCharacter
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserCharacter extends EntityPathBase<UserCharacter> {

    private static final long serialVersionUID = -1607945444L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserCharacter userCharacter = new QUserCharacter("userCharacter");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final ListPath<ExerciseLog, QExerciseLog> exerciseLogs = this.<ExerciseLog, QExerciseLog>createList("exerciseLogs", ExerciseLog.class, QExerciseLog.class, PathInits.DIRECT2);

    public final ComparablePath<Character> gender = createComparable("gender", Character.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Short> points = createNumber("points", Short.class);

    public final QUser user;

    public final NumberPath<Integer> userExperience = createNumber("userExperience", Integer.class);

    public final NumberPath<Short> userLevel = createNumber("userLevel", Short.class);

    public final StringPath userNickname = createString("userNickname");

    public QUserCharacter(String variable) {
        this(UserCharacter.class, forVariable(variable), INITS);
    }

    public QUserCharacter(Path<? extends UserCharacter> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserCharacter(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserCharacter(PathMetadata metadata, PathInits inits) {
        this(UserCharacter.class, metadata, inits);
    }

    public QUserCharacter(Class<? extends UserCharacter> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

