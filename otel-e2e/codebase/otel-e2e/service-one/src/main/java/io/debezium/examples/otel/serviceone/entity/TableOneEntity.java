package io.debezium.examples.otel.serviceone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TABLE_ONE", uniqueConstraints = {
        @UniqueConstraint(name = "table_one_uk", columnNames = { "DATA", "DELETED" }) })
@Where(clause = "DELETED=0")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TableOneEntity implements Serializable {

    private static final long serialVersionUID = 1745318439438792641L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "DATA", nullable = false)
    private String data;

    @Column(name = "CREATED_DT", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "UPDATED_DT")
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date updatedOn;

    @Column(name = "DELETED", nullable = false)
    private long deleted = 0;

    @Column(name = "tracingspancontext")
    private String tracingspancontext;

}
