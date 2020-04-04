package com.mibanco.microservicio.app.person.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tb_personas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="document")
	private String document;
	@Column(name="finger_print")
	private Boolean fingerprint;
	@Column(name="black_list")
	private Boolean blackist;
	
}
