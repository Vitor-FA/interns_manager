package br.com.manager.interns.API.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "Leads")
public class LeadDomain {

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

  @OneToMany()
  @Fetch(FetchMode.SUBSELECT)
  @JoinTable(name="lead_intern",
      joinColumns=@JoinColumn(name="lead_identifier"),
      inverseJoinColumns=@JoinColumn(name="intern_identifier"))
  @JsonBackReference
  private List<InternsDomain> interns = new ArrayList<>();

  public void addInterns(List<InternsDomain> foundInternsList) {
    interns.addAll(foundInternsList);
  }

  public void removeInterns(List<InternsDomain> foundInternsList) {
    interns.removeAll(foundInternsList);
  }
}
