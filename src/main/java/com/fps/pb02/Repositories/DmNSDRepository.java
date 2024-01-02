package com.fps.pb02.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.fps.pb02.Object.DmNSD;


@Transactional(rollbackFor = {Exception.class })
public interface DmNSDRepository extends JpaRepository<DmNSD, String> {
}
