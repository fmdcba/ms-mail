package com.mindhub.ms_mail.dtos;

public record OrderItemDTO(Long id, Long orderId, Long productId, Integer quantity) {
}
