package com.example.spacelab.model.chat;

import com.example.spacelab.model.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Entity
@DiscriminatorValue("PRIVATE")
@Accessors(chain = true)
public class PrivateChat extends Chat {

    @ManyToOne
    @JoinColumn(name = "person1_id", referencedColumnName = "id")
    private UserEntity person1;

    @ManyToOne
    @JoinColumn(name = "person2_id", referencedColumnName = "id")
    private UserEntity person2;

    public ChatType getType() {
        return ChatType.PRIVATE;
    }

//    public String getName() {
//        return person1.getUserEntityName() + " | " + person2.getUserEntityName();
//    }
//    public String getIcon() { return person1.getAvatar() + " | " + person2.getAvatar();}

}
