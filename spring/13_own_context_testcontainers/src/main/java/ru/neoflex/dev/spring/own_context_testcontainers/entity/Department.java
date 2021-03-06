package ru.neoflex.dev.spring.own_context_testcontainers.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "DEPARTMENT")
@SequenceGenerator(name = "DEPARTMENT_ID_SEQ", sequenceName = "DEPARTMENT_ID_SEQ", allocationSize = 1)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENT_ID_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<Employee> employees;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }
}