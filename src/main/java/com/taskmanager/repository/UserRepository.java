package com.taskmanager.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.v1.FirestoreAdminClient;
import com.google.cloud.firestore.v1beta1.FirestoreSettings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import com.google.firebase.database.Query;
import com.taskmanager.exception.AppException;
import com.taskmanager.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private static Logger log = LoggerFactory.getLogger(UserRepository.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    private static DatabaseReference databaseReference = null;

//    private static Firestore firestore;

    private static FirebaseDatabase firebaseDatabase;

    private static User currentUser;


    private void init() throws Exception {
        if (databaseReference != null) return;

        log.info("connecting to firebase");
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl("https://task-manager-ae095.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(firebaseOptions);
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        firestore = FirestoreClient.getFirestore();
//        Iterable<CollectionReference> collections = firestore.listCollections();
//        for (CollectionReference collection : collections) {
//            log.info("Collection::{}", collection.getId());
//        }
        firebaseDatabase = databaseReference.getDatabase();
        log.info("connected to firebase. Database:{}", databaseReference);
    }

    public void create(User user) {
        try {
            init();
            ApiFuture<Void> future = databaseReference.child("users").child(user.getId()).setValueAsync(user);
            future.get();
        } catch (Exception e) {
            log.error("unable to write to firebase. User:{} error:{}", user, e);
            throw new AppException("unable to write to firebase", e);
        }
    }

    public User getUser(String userId) {
        try {
//            DatabaseReference userReference  = databaseReference.child("users").child(userId);
//            FirebaseDatabase firebaseDatabase = userReference.getDatabase();
            init();
//            log.info("user id:{}", userId);
//            DocumentReference documentReference = firestore.collection("users").document(userId);
//            log.info("document reference:{}", documentReference.getPath());
//            log.info("document reference contains information:{}", documentReference);
//            ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
//            log.info("makes it to APIFuture");
//            DocumentSnapshot documentSnapshot = null;
//            try {
//                documentSnapshot = apiFuture.get();
//                log.info("makes it to documentSnapshot::{}", documentSnapshot.getData());
//            } catch (Exception e) {
//                log.error("apiFuture.get() error :{}", e.getMessage());
//                e.printStackTrace();
//            }
//            User user = documentSnapshot.toObject(User.class);
//            log.info("created user:{}", user.toString());
//            log.info("firebaseDatabase.getReference():{}", firebaseDatabase.getReference());
//            DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(userId);
//            log.info("databaseReference.toString():{} databaseReference.getRoot():{}", databaseReference.toString(), databaseReference.getRoot());
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        values.add(dataSnapshot);
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError error) {
//
//                }
//            });
//            log.info("values:{}", values);
//            databaseReference.getKey();
            DatabaseReference usersReference = databaseReference.child("users");
            Query query = usersReference.orderByChild("user_id").equalTo(userId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        currentUser = childSnapshot.getValue(User.class);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
            log.info("current user information:{}", currentUser.toString());
        } catch (Exception e) {
            log.error("unable to find user. Username:{}, Error:{}", userId, e.getMessage());
            throw new AppException("unable to find user", e);
        }
        return new User();
    }
}
