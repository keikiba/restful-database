package com.keikiba.springboot.repositories;

import com.keikiba.springboot.MyData;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyDataRepository extends JpaRepository<MyData, Long> {
	
	// JpaRepository によるメソッド処理の自動生成
	public Optional<MyData> findById(Long name);
	public List<MyData> findByNameLike(String name); // あいまい検索 "%str%"
	public List<MyData> findByIdIsNotNullOrderByIdDesc(); // IDが非null全部 → 全エントリ、降順
	public List<MyData> findByAgeGreaterThan(Integer age); // より大きいエントリ
	public List<MyData> findByAgeBetween(Integer age1, Integer age2); // 指定範囲内のエントリ

}

