package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserStatsHistory is a Querydsl query type for UserStatsHistory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserStatsHistory extends EntityPathBase<UserStatsHistory> {

    private static final long serialVersionUID = 640562722L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserStatsHistory userStatsHistory = new QUserStatsHistory("userStatsHistory");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Integer> absStats = createNumber("absStats", Integer.class);

    public final NumberPath<Integer> armsStats = createNumber("armsStats", Integer.class);

    public final NumberPath<Integer> backStats = createNumber("backStats", Integer.class);

    public final NumberPath<Integer> chestStats = createNumber("chestStats", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> legsStats = createNumber("legsStats", Integer.class);

    public final NumberPath<Integer> staminaStats = createNumber("staminaStats", Integer.class);

    public final DatePath<java.time.LocalDate> statsDate = createDate("statsDate", java.time.LocalDate.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final QUserCharacter userCharacter;

    public QUserStatsHistory(String variable) {
        this(UserStatsHistory.class, forVariable(variable), INITS);
    }

    public QUserStatsHistory(Path<? extends UserStatsHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserStatsHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserStatsHistory(PathMetadata metadata, PathInits inits) {
        this(UserStatsHistory.class, metadata, inits);
    }

    public QUserStatsHistory(Class<? extends UserStatsHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userCharacter = inits.isInitialized("userCharacter") ? new QUserCharacter(forProperty("userCharacter"), inits.get("userCharacter")) : null;
    }

}

