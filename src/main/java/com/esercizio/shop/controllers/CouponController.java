package com.esercizio.shop.controllers;

import com.esercizio.shop.entities.Cart;
import com.esercizio.shop.entities.Coupon;
import com.esercizio.shop.entities.SubCategory;
import com.esercizio.shop.repositories.CartRepository;
import com.esercizio.shop.repositories.CouponRepository;
import com.esercizio.shop.repositories.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value ="/coupon")
@CrossOrigin(origins = "http://localhost:4200")
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CartRepository cartRepository;


    @PostMapping("/add")
    public Coupon addCoupon(@RequestParam String code, int percentageSale, int idSubcategory) {
        Coupon coupon = new Coupon();
        coupon.setCode(code);
        coupon.setPercentageSale(percentageSale);
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(idSubcategory);
        if (subCategoryOptional.isPresent()) {
            SubCategory subCategory = subCategoryOptional.get();
            coupon.setSubCategory(subCategory);
        }
        couponRepository.save(coupon);
        return coupon;
    }

    @PostMapping
    public Cart applyCoupon(@RequestParam int idUser, String couponCode) {
        Optional<Cart> cartOpt = cartRepository.findByUser_Id(idUser);
        Optional<Coupon> couponOpt = couponRepository.findByCode(couponCode);
        if (couponOpt.isPresent()) {
            Coupon coupon = couponOpt.get();
            if (cartOpt.isPresent()) {
                Cart cart = cartOpt.get();
                // Gestione sconto
                return cart;
            }
        }
        return null;
    }
}
