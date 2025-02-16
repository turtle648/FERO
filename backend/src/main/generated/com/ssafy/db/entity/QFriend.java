package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QFriend is a Querydsl query type for Friend
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFriend extends EntityPathBase<Friend> {

    private static final long serialVersionUID = 1348250848L;

    public static final QFriend friend = new QFriend("friend");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath createdAt = createString("createdAt");

    public final StringPath friendId = createString("friendId");

    public final NumberPath<Integer> friendLevel = createNumber("friendLevel", Integer.class);

    public final StringPath friendNickname = createString("friendNickname");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final EnumPath<Friend.FriendStatus> status = createEnum("status", Friend.FriendStatus.class);

    public final StringPath userId = createString("userId");

    public QFriend(String variable) {
        super(Friend.class, forVariable(variable));
    }

    public QFriend(Path<? extends Friend> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFriend(PathMetadata metadata) {
        super(Friend.class, metadata);
    }

}

