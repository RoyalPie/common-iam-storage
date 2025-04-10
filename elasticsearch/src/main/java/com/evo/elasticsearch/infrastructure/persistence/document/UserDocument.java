package com.evo.elasticsearch.infrastructure.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.UUID;

@Document(indexName = "user-document")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private UUID userID;
    @Field(type = FieldType.Keyword)
    private UUID keycloakUserId;
    @MultiField(mainField = @Field(type = FieldType.Text),
            otherFields = {
                    @InnerField(suffix = "sort", type = FieldType.Keyword)
            })
    private String username;
    @Field(type = FieldType.Text)
    private String email;
    @Field(type = FieldType.Keyword)
    private String firstName;
    @Field(type = FieldType.Keyword)
    private String lastName;
    @Field(type = FieldType.Keyword)
    private UUID avatarFileId;
    @Field(type = FieldType.Date)
    private Date dateOfBirth;
    @Field(type = FieldType.Keyword)
    private String address;
    @Field(type = FieldType.Integer)
    private int yearsOfExperience;
    @Field(type = FieldType.Keyword)
    private boolean active;
    @Field(type = FieldType.Keyword)
    private boolean deleted;

}
