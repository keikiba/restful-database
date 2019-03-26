package com.keikiba.springboot;

import java.util.List;

import javax.persistence.CascadeType;

//import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/*
Entityを使ってDBアクセス
  * 「リポジトリ」を用意すればよい → 基本的なアクセス、CRUD、自動生成できる検索
  * もっと自在にアクセスするには Spring Data JPA (Java Persistence API)
    - DAOオブジェクトを利用。DBアクセスの機能をまとめ、コントローラーから呼び出す
    - リポジトリをそのまま拡張も良いが、より低レベルなJPA機能を利用するにはDAOクラスを作成・実装
      (ex. MyDataDao, MyDataDaoImpl) EntityManager

JPQL言語
  * @NamedQuery
    - エンティティクラスにクエリ文。DAOのコードから切り離せる。エンティティ＋検索機能
  * @Query
    - リポジトリインターフェースにクエリアノテーション
  * @NamedQueryも@Queryも内部処理は同じ。
    - アノテーションを用意する場所が違う
    - エンティティ自体にクエリーをもたせるのが良いか、リポジトリに用意するのが良いか、アプリ設計による
    
Criteria API
  * Javaらしいデータベースアクセス
    CriteriaBuilder
    CriteriaQuery
    Root
    

*/

@Entity
@Table(name="mydata")
public class MyData {

	// Association with MsgData
	@OneToMany(cascade=CascadeType.ALL)
	@Column(nullable = true)
	private List<MsgData> msgdatas;
	
	public List<MsgData> getMsgdatas() {
		return msgdatas;
	}
	public void setMsgdatas(List<MsgData> msgdatas) {
		this.msgdatas = msgdatas;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@NotNull
	private long id;
	
	@Column(length = 50, nullable = false)
	@NotEmpty(message="空白は不可（とメッセージをハードコードすることも）")
	private String name;
	
	@Column(length = 200, nullable = true)
	@Email
	private String mail;

	@Column(nullable = true)
	@Min(0)
	@Max(200)
	private Integer age;
	
	@Column(nullable = true)
	
	private String memo;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

}
