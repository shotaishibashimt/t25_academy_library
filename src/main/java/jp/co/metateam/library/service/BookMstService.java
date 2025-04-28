package jp.co.metateam.library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.micrometer.common.util.StringUtils;
import jp.co.metateam.library.model.AccountDto;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.repository.BookMstRepository;

@Service
public class BookMstService {

    private final BookMstRepository bookMstRepository;

    @Autowired

    //新たに追加↓
    // private BookMstRepository bookRepository;

    // public List<BookMst> getAllBooks() {
    //     return bookMstRepository.findLimitedBook();
    // }

    // public Optional<BookMst> getBookById(Long id) {
    //     return bookMstRepository.selectById(id);
    // }
//ここまで
    public BookMstService(BookMstRepository bookMstRepository) {
        this.bookMstRepository = bookMstRepository;
    }

    // 追加した↓
    public BookMst selectByIsbn(String isbn) {
        return bookMstRepository.findByIsbn(isbn);
    }



    public List<BookMstDto> findAvailableWithStockCount() {
        List<BookMst> books = this.bookMstRepository.findLimitedBook();
        List<BookMstDto> bookMstDtoList = new ArrayList<BookMstDto>();

        // 書籍の在庫数を取得
        // FIXME: 現状は書籍ID毎にDBに問い合わせている。一度のSQLで完了させたい。
        for (int i = 0; i < books.size(); i++) {
            BookMst book = books.get(i);
            BookMstDto bookMstDto = new BookMstDto();
            bookMstDto.setId(book.getId());
            bookMstDto.setIsbn(book.getIsbn());
            bookMstDto.setTitle(book.getTitle());
            bookMstDtoList.add(bookMstDto);
        }

        return bookMstDtoList;
    }

    @Transactional
    public void save(BookMstDto BookMstDto) {
        try {
            // AccountDtoからAccountへの変換
            BookMst bookMst = new BookMst();

            bookMst.setTitle(BookMstDto.getTitle());
            bookMst.setIsbn(BookMstDto.getIsbn());

            // データベースへの保存
            this.bookMstRepository.save(bookMst);
        } catch (Exception e) {
            throw e;
        }
    }
}
