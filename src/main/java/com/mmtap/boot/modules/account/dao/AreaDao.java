package com.mmtap.boot.modules.account.dao;

import com.mmtap.boot.modules.account.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaDao extends JpaRepository<Area,Integer> {
}
