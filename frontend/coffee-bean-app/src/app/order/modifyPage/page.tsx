"use client"
import { useSearchParams } from "next/navigation";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Link from 'next/link';
import {GetResMenuOrderDto, GetResDetailOrderDto } from "./types";

export default function Page() {

    const [order, setOrder] = useState<GetResMenuOrderDto | null>(null);

    // sessionStorage에서 데이터 불러오기
    useEffect(() => {
        const storedOrder = sessionStorage.getItem("selectedOrder");
        if (storedOrder) {
            setOrder(JSON.parse(storedOrder)); // JSON 문자열을 객체로 변환
        }
    }, []);

    if (!order) {
        return <p className="text-center text-gray-500">📦 주문 정보를 불러오는 중...</p>;
    }

    return (
        <div className="container mx-auto px-4">
            <h1 className="text-2xl font-bold text-center my-8">주문 수정</h1>

            <div className="bg-white shadow-lg rounded-lg p-6">
                <h2 className="text-xl font-bold mb-4">주문 정보</h2>
                <p className="text-gray-600">ID: {order.id}</p>
                <p className="text-gray-600">이메일: {order.email}</p>
                <p className="text-gray-600">주소: {order.address}</p>
                <p className="text-gray-600">우편번호: {order.postCode}</p>

                <h3 className="text-lg font-semibold mt-6">주문 상세</h3>
                <ul className="space-y-2 mt-2">
                    {order.orders.length > 0 ? (
                        order.orders.map((detail) => (
                            <li key={detail.id} className="flex justify-between bg-gray-100 p-2 border rounded-lg">
                                <span>{detail.name}</span>
                                <span className="font-semibold">{detail.quantity}개</span>
                            </li>
                        ))
                    ) : (
                        <li className="text-gray-500">주문 상세 없음</li>
                    )}
                </ul>
            </div>
        </div>
    );
}