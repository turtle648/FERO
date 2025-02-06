package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserStats is a Querydsl query type for UserStats
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserStats extends EntityPathBase<UserStats> {

    private static final long serialVersionUID = 700427794L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserStats userStats = new QUserStats("userStats");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Short> absStats = createNumber("absStats", Short.class);

    public final NumberPath<Short> armsStats = createNumber("armsStats", Short.class);

    public final NumberPath<Short> backStats = createNumber("backStats", Short.class);

    public final NumberPath<Short> chestStats = createNumber("chestStats", Short.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Short> legsStats = createNumber("legsStats", Short.class);

    public final NumberPath<Short> staminaStats = createNumber("staminaStats", Short.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final QUser user;

    public QUserStats(String variable) {
        this(UserStats.class, forVariable(variable), INITS);
    }

    public QUserStats(Path<? extends UserStats> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserStats(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserStats(PathMetadata metadata, PathInits inits) {
        this(UserStats.class, metadata, inits);
    }

    public QUserStats(Class<? extends UserStats> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

