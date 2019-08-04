package com.musiy.dao;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;
import com.musiy.mongoservice.ObservableSubscriber;
import org.bson.Document;

import java.util.Collection;
import java.util.stream.Collectors;

public class UsersDaoMongo implements UsersDao {

    private final MongoCollection<Document> collection;

    public UsersDaoMongo(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public Collection<UserDto> getUsers() {
        ObservableSubscriber<Document> subscriber = new ObservableSubscriber<>();
        collection.find().subscribe(subscriber);
        try {
            subscriber.await();
        } catch (Throwable throwable) {
            throw new RuntimeException("Error while obtaining result from mongodb");
        }
        return subscriber.getResults().stream()
                .map(d -> {
                    UserDto userDto = new UserDto();
                    userDto.setName(d.get("user_name").toString());
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void saveUser(UserDto userDto) {
        ObservableSubscriber<Success> subscriber = new ObservableSubscriber<>();
        Document doc = new Document("key", System.currentTimeMillis())
                .append("user_name", userDto.getName());
        collection.insertOne(doc).subscribe(subscriber);
        try {
            subscriber.await();
        } catch (Throwable throwable) {
            throw new RuntimeException("Error while obtaining result from mongodb");
        }
    }

    @Override
    public void deleteUser(UserDto userDto) {

    }
}
