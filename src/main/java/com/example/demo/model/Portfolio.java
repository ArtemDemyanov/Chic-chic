package com.example.demo.model;

import jakarta.persistence.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Портфолио")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @lombok.Getter
    @lombok.Setter
    Long id;

    @lombok.Setter
    @lombok.Getter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "пользователь_id")
    User user;

    @lombok.Setter
    @lombok.Getter
    private int experience;

    @lombok.Setter
    @lombok.Getter
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private List<byte[]> certificates;

    @lombok.Getter
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photos;

    @lombok.Setter
    @lombok.Getter
    String description;

    @lombok.Setter
    @lombok.Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ModerationStatus status = ModerationStatus.PENDING;

    public Portfolio(){
        super();
    }

    public Portfolio(User user, int experience, List<byte[]> certificates, byte[] photos, String description ) {
        this.user = user;
        this.experience = experience;
        this.certificates = certificates;
        this.photos = photos;
        this.description = description;
    }

    public void setPhotosAsList(List<byte[]> photosList) {
        this.photos = serializePhotos(photosList);
    }

    public List<byte[]> getPhotosAsList() {
        return deserializePhotos(this.photos);
    }

    private byte[] serializePhotos(List<byte[]> photosList) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(photosList);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сериализации списка фотографий", e);
        }
    }

    private List<byte[]> deserializePhotos(byte[] photoBytes) {
        if (photoBytes == null) {
            return new ArrayList<>();
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(photoBytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            @SuppressWarnings("unchecked")
            List<byte[]> photosList = (List<byte[]>) ois.readObject();
            return photosList;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка при десериализации списка фотографий", e);
        }
    }

}