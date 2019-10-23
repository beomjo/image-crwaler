# ImageCrwaler

<br/>
<br/>


### 이미지 크롤링하기
[이미지를 크롤링할 사이트](https://www.gettyimages.com/photos/collaboration?sort=best&mediatype=photography&phrase=collaboration)  
이미지를 크롤링하여 페이징처리, 그리드 (Column 3)로 보여주기  


<br/>

### Used Stack
**Jsoup, Paging(PagedKeyDataSource), RxJava(RxKotlin), Mvvm(Databinding), Glide**

<br/>

### Image  
<img src="https://user-images.githubusercontent.com/39984656/67354121-3cc23600-f58f-11e9-80ca-8d2b40433242.png" width="300" height="450">  

<br/>

### Example Crawling used Jsoup
```
 private fun scrap(page: Int): Observable<String> {
        return Observable.create<String> { emitter ->
            val doc = Jsoup
                .connect(
                    "https://www.gettyimages.com/photos/collaboration?" +
                            "mediatype=photography&page=${page}&phrase=collaboration&sort=mostpopular"
                )
                .userAgent("Chrome")
                .get()

            val elements = doc.select(".search-content__gallery-assets").select("img")

            for (e in elements) {
                emitter.onNext(e.attr("src"))
            }
            emitter.onComplete()
        }
    }
```  

<br/>
<br/>

### Jsoup  
add Dependency  
```
dependencies {
    implementation 'org.jsoup:jsoup:1.12.1'
}
```

Jsoup mainly Class  

Class Name | description
--------- | ---------
Document | Jsoup 얻어온 결과 HTML 전체문서
Element | Document의 HTML요소
Elements | Element가 모인 자료형, for이나 while등 반복문을 사용하여 element를 꺼낼수 있음
Connection | Jsoup의 connect혹은 설정 메소드들을 이용하여 만들어지는 객체, 연결을 하기 위한 정보를 담고있음
Response | Jsoup이 URL에 접속하여 얻어온 결과. Document와 다르게 State Code, State Message, Charset같은 헤더메시지나 쿠키등을 가지고있음  

select method overview
- tagname: 태그별로 요소 찾기 a
- ns|tag: 네임 스페이스에서 태그로 요소 찾기, ex : 요소 fb|name찾기<fb:name>
- #id: ID로 요소 찾기, ex : #logo
- .class: 클래스 이름으로 요소 찾기, ex : .masthead
- [attribute]: 속성을 가진 요소, ex : [href]
- [^attr]: 속성 이름 접두사 [^data-]가있는 요소 ( 예 : HTML5 데이터 세트 속성이있는 요소를 찾습니다)
- [attr=value]: 속성 값을 가진 요소, 예 : [width=500]( 와 같이 할당 가능 [data-name='launch sequence'])
- [attr^=value], [attr$=value], [attr*=value]:로 시작하는 속성으로 끝나는, 또는, 예를 들어 값을 포함와 요소[href*=/path/]
- [attr~=regex]: 정규식과 일치하는 속성 값을 가진 요소,  예 :img[src~=(?i)\.(png|jpe?g)]
- * : 모든 요소, ex : *  

<br/>

select combinations  
- el#id: ID가있는 요소, ex. div#logo
- el.class: 클래스가있는 요소, ex. div.masthead
- el[attr]: 속성을 가진 요소, ex. a[href]
- 모든 조합, ex : a[href].highlight
- ancestor child: 상위 항목에서 내려 오는 하위 요소 (ex : "body"클래스가있는 블록 아래에서 요소를 .body p찾습니다.p
- parent > child: 부모로부터 직접 내려 오는 자식 요소, ex . 요소를 div.content > p찾습니다 p. 및 body > *body 태그의 직접적인 아이를 찾습니다
- siblingA + siblingB: 형제 B 요소 바로 앞에 형제 A가 있음을 찾습니다. ex : div.head + div
- siblingA ~ siblingX: 형제 A 앞에있는 형제 X 요소를 찾습니다. ex : h1 ~ p
- el, el, el: 여러 선택기를 그룹화하고 선택기와 일치하는 고유 한 요소를 찾습니다. ex :div.masthead, div.logo

<br/>

[Link Jsoup Document](https://jsoup.org/cookbook/extracting-data/selector-syntax)  

