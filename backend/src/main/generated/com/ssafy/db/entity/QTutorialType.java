package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTutorialType is a Querydsl query type for TutorialType
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTutorialType extends EntityPathBase<TutorialType> {

    private static final long serialVersionUID = 1551677562L;

    public static final QTutorialType tutorialType = new QTutorialType("tutorialType");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath tutorialName = createString("tutorialName");

    public QTutorialType(String variable) {
        super(TutorialType.class, forVariable(variable));
    }

    public QTutorialType(Path<? extends TutorialType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTutorialType(PathMetadata metadata) {
        super(TutorialType.class, metadata);
    }

}

