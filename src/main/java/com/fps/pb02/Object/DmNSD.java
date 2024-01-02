package com.fps.pb02.Object;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "checkin_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class DmNSD {

	@Column(name = "user_name")
	private String username;
	
	@Id
	@Column(name = "pass_word")
	private String password;

	
	@Column(name = "device_id")
	private String deviceId;
	
	@Column(name = "device_name")
	private String deviceName;
	
	@Column(name = "ip_gateway")
	private String ipGateway;
	
	@Column(name = "type")
	private String type;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getIpGateway() {
		return ipGateway;
	}

	public void setIpGateway(String ipGateway) {
		this.ipGateway = ipGateway;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
