package tr.edu.eskisehir.camishani.dataacquisition.jpa.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tr.edu.eskisehir.camishani.dataacquisition.cfiltering.SimilarityMeasurable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements SimilarityMeasurable, Serializable, UserDetails {

    private int id;
    private String username;
    private String password;
    private List<String> authority = new ArrayList<>();
    private List<Rating> ratings;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ElementCollection
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    @JsonIgnore
    public List<String> getAuthority() {
        return authority;
    }

    public void setAuthority(List<String> authorities) {
        this.authority = authorities;
    }


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Transient
    public int getTotalRatings() {
        return getRatings().size();
    }

    @Override
    @Transient
    @JsonIgnore
    public Iterable<Rating> getSimilarityFactors() {
        return getRatings();
    }

    @Transient
    @JsonIgnore
    @Override
    public int getMaxFactorId() {
        int max = 0;
        for (Rating r : getSimilarityFactors()) {
            if (r.getMovie().getId() > max)
                max = r.getMovie().getId();
        }
        return max;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthority().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
