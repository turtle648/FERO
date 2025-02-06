package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 846542477L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isTemporaryPw = createBoolean("isTemporaryPw");

    public final BooleanPath isValid = createBoolean("isValid");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ListPath<UserTutorialProgress, QUserTutorialProgress> tutorialProgress = this.<UserTutorialProgress, QUserTutorialProgress>createList("tutorialProgress", UserTutorialProgress.class, QUserTutorialProgress.class, PathInits.DIRECT2);

    public final QUserCharacter userCharacter;

    public final StringPath userEmail = createString("userEmail");

    public final StringPath userId = createString("userId");

    public final StringPath userName = createString("userName");

    public final StringPath userPassword = createString("userPassword");

    public final QUserStats userStats;

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userCharacter = inits.isInitialized("userCharacter") ? new QUserCharacter(forProperty("userCharacter"), inits.get("userCharacter")) : null;
        this.userStats = inits.isInitialized("userStats") ? new QUserStats(forProperty("userStats"), inits.get("userStats")) : null;
    }

}

