package com.jlusoft.lazybone;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {

	private static final long serialVersionUID = -1954592447845975140L;

	private String author;// 责任者
	private String bookTitle;// 书箱名称
	private String bookType;// 图书类型
	private String canLendNumber; // 可借副本数量
	private String classifiedNo;// 中图法分类号
	private String collectionNumber;// 馆藏副本数量
	private String coverUrl;// 图片链接
	private String ISBN;// ISBN
	private String marcNo;// 图书marc，图书的唯一标识(标准)
	private String pressAndIssue;// 出版发行项
	private String price;// 定价
	private List<String> relatedBooks;// 相关借阅

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}


	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public String getCanLendNumber() {
		return canLendNumber;
	}

	public void setCanLendNumber(String canLendNumber) {
		this.canLendNumber = canLendNumber;
	}

	public String getClassifiedNo() {
		return classifiedNo;
	}

	public void setClassifiedNo(String classifiedNo) {
		this.classifiedNo = classifiedNo;
	}

	public String getCollectionNumber() {
		return collectionNumber;
	}

	public void setCollectionNumber(String collectionNumber) {
		this.collectionNumber = collectionNumber;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getMarcNo() {
		return marcNo;
	}

	public void setMarcNo(String marcNo) {
		this.marcNo = marcNo;
	}

	public String getPressAndIssue() {
		return pressAndIssue;
	}

	public void setPressAndIssue(String pressAndIssue) {
		this.pressAndIssue = pressAndIssue;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<String> getRelatedBooks() {
		return relatedBooks;
	}

	public void setRelatedBooks(List<String> relatedBooks) {
		this.relatedBooks = relatedBooks;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
