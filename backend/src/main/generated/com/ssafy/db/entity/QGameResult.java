package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QGameResult is a Querydsl query type for GameResult
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGameResult extends EntityPathBase<GameResult> {

    private static final long serialVersionUID = -767921167L;

    public static final QGameResult gameResult = new QGameResult("gameResult");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> duration = createNumber("duration", Integer.class);

    public final NumberPath<Long> exerciseId = createNumber("exerciseId", Long.class);

    public final StringPath gameId = createString("gameId");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath opponentId = createString("opponentId");

    public final NumberPath<Short> opponentScore = createNumber("opponentScore", Short.class);

    public final EnumPath<GameResult.GameResultType> result = createEnum("result", GameResult.GameResultType.class);

    public final StringPath userId = createString("userId");

    public final NumberPath<Short> userScore = createNumber("userScore", Short.class);

    public QGameResult(String variable) {
        super(GameResult.class, forVariable(variable));
    }

    public QGameResult(Path<? extends GameResult> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGameResult(PathMetadata metadata) {
        super(GameResult.class, metadata);
    }

}

