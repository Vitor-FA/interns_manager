package br.com.manager.interns.API.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "Interns")
public class InternsDomain {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Email
  private String email;

  @ManyToMany(mappedBy = "interns")
  @JsonBackReference
  private List<BuddysDomain> buddys = new ArrayList<>();

  @ManyToOne(cascade = CascadeType.ALL)
  @JsonBackReference
  private LeadDomain lead;

  public void addBuddy(BuddysDomain buddyDomain) {
    buddys.add(buddyDomain);
  }

  public void removeBuddy(BuddysDomain buddyDomain) {
    buddys.remove(buddyDomain);
  }


  public void removeLead() {
    lead = null;
  }
}
