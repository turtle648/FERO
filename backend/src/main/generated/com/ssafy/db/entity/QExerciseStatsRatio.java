package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QExerciseStatsRatio is a Querydsl query type for ExerciseStatsRatio
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QExerciseStatsRatio extends EntityPathBase<ExerciseStatsRatio> {

    private static final long serialVersionUID = 840656358L;

    public static final QExerciseStatsRatio exerciseStatsRatio = new QExerciseStatsRatio("exerciseStatsRatio");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Float> absRatio = createNumber("absRatio", Float.class);

    public final NumberPath<Float> armsRatio = createNumber("armsRatio", Float.class);

    public final NumberPath<Float> backRatio = createNumber("backRatio", Float.class);

    public final NumberPath<Float> chestRatio = createNumber("chestRatio", Float.class);

    public final StringPath exerciseType = createString("exerciseType");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Float> legsRatio = createNumber("legsRatio", Float.class);

    public final NumberPath<Float> staminaRatio = createNumber("staminaRatio", Float.class);

    public QExerciseStatsRatio(String variable) {
        super(ExerciseStatsRatio.class, forVariable(variable));
    }

    public QExerciseStatsRatio(Path<? extends ExerciseStatsRatio> path) {
        super(path.getType(), path.getMetadata());
    }

    public QExerciseStatsRatio(PathMetadata metadata) {
        super(ExerciseStatsRatio.class, metadata);
    }

}

