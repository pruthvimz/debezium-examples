package io.debezium.examples.otel.serviceone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "TABLE_TWO", uniqueConstraints = {
        @UniqueConstraint(name = "table_two_uk", columnNames = { "DATA", "DELETED" }) })
@Where(clause = "DELETED=0")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TableTwoEntity {

    private static final long serialVersionUID = 2745318439438792641L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "DATA", nullable = false)
    private String data;

    @Column(name = "DELETED", nullable = false)
    private long deleted = 0;

}
