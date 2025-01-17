"use client"
import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { createOrder } from "./api";
import { PostOrderRequestDto, CustomerDto, PostDetailOrderDto } from "./types";


const coffeeBeans = [
  { id: 1, src: "/images/coffeebean1.png", name: "Columbia Nariñó", price: 5000 },
  { id: 2, src: "/images/coffeebean2.png", name: "Brazil Serra Do Caparao", price: 5500 },
  { id: 3, src: "/images/coffeebean3.png", name: "Columbia Quindio", price: 6000 },
  { id: 4, src: "/images/coffeebean4.png", name: "Ethiopia Sidamo", price: 6500 },
];



export default function Home() {

  const router = useRouter();
  const [order, setOrder] = useState<PostOrderRequestDto>({
    customer: {
      email: "",
      address: "",
      postcode: ""
    },
    products: []
    
  });

  // 페이지 조작 자바 스크립트트
  const [quantities, setQuantities] = useState(Array(coffeeBeans.length).fill(0));
  const [individualTotals, setIndividualTotals] = useState(Array(coffeeBeans.length).fill(0));
  const [totalPrice, setTotalPrice] = useState(0);

  const increaseQuantity = (index: number) => {
    const newQuantities = [...quantities];
    newQuantities[index] += 1;
    setQuantities(newQuantities);
  };

  // 수량 감소 함수 (최소 1개 유지)
  const decreaseQuantity = (index: number) => {
    const newQuantities = [...quantities];
    if (newQuantities[index] > 0) {
      newQuantities[index] -= 1;
      setQuantities(newQuantities);
    }
  };

 useEffect(() => {
    const newIndividualTotals = coffeeBeans.map((item, index) => quantities[index] * item.price) ;
    setIndividualTotals(newIndividualTotals);

    // 전체 총액 계산
    const total = newIndividualTotals.reduce((acc, curr) => acc + curr, 0);
    setTotalPrice(total);
  }, [quantities]);


  // http 메세지 전송 자바 스크립트
  
  useEffect(() => {
    if (order.products.length > 0) {
      sendOrder(order);
    }
  }, [order]); // `order`가 변경될 때마다 실행

  const sendOrder = async (updatedOrder: PostOrderRequestDto) => {
      const response = await createOrder(updatedOrder);
      
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();  

    const { id, value } = e.target;
    setOrder((prev) => ({
      ...prev,
      customer: { ...prev.customer, [id]: value }, // email, address, postcode 업데이트
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const isConfirmed = window.confirm("결제 하시겠습니까?");
    if (!isConfirmed) return;

    const updatedProducts: PostDetailOrderDto[] = coffeeBeans
      .map((item, index) => ({
        id: item.id,
        quantity: quantities[index],
      }))
      .filter((product) => product.quantity > 0); 
    
      if (updatedProducts.length === 0) {
        alert("선택된 상품이 없습니다.");
        return;
      }

      setOrder((prevOrder) => ({
        ...prevOrder,
        products: updatedProducts,
      }));
  
     //console.log("업데이트된 주문:", order); // 업데이트된 order 확인
    
  };

  

  return (
    //1차차 레이아웃
    <div className="container mx-auto px-4">
        {/* 사이트 이름름 */}
      <div className="text-center my-8">
        <h1 className="text-2xl font-bold" role="button" 
        onClick={() => router.push("/")} >Grids & Circle</h1>
      </div>

       {/* 2차 레이아웃웃 */}
      <div className="bg-white shadow-lg rounded-lg">
        {/* 상품 목록 */}
        <div className="grid grid-cols-1 md:grid-cols-3">
          
          <div className="md:col-span-2 p-4">
            <h5 className="font-bold text-lg mb-4">상품 목록</h5>
            {/* 상품 목록 나열열 */}
            <ul className="space-y-4">
              {coffeeBeans.map((item, index) => (
                //index, 사진 src, 커피콩 이름, 가격 리스트로 정리리
                <li key={index} className="flex items-center bg-gray-100 p-4 rounded-lg">
                  {/* 사진 */}
                  <div className="w-16">
                    <Image
                      src={item.src}
                      alt="Product Image"
                      width={56}
                      height={56}
                      className="rounded"
                    />
                  </div>
                  {/* 상품 이름 */}
                  <div className="flex-1 ml-4">
                    <div className="text-gray-500">커피콩</div>
                    <div className="font-semibold">{item.name}</div>
                  </div>
                  {/* 상품 가격 */}
                  <div className="text-center font-semibold text-gray-700">{item.price}원</div>
                  {/* 상품 추가 버튼 */}
                  <div className="flex items-center space-x-2 ml-4">
                  <button 
                    onClick={() => decreaseQuantity(index)}
                    className="bg-gray-300 text-gray-700 px-3 py-2 rounded"
                  >
                  -
                  </button>
                  <span className="w-6 text-center">{quantities[index]}</span>
                    <button 
                    onClick={() => increaseQuantity(index)}
                    className="bg-gray-300 text-gray-700 px-3 py-2 rounded"
                    >
                  +
                  </button>
          </div>
                </li>
              ))}
              {/* li 종료 */}
            </ul>

          </div>

          {/* Summary */}
          <div className="bg-gray-100 rounded-r-lg p-6">
            <h5 className="font-bold text-lg mb-4">Summary</h5>
            <hr className="mb-4" />
            {/* 선택한 상품 목록 */}
            {coffeeBeans.map((item, index) => (
              <div key={index} className="flex justify-between mb-2">
                <span className="text-left" >{item.name}</span>
                <div className="flex space-x-4">
                  <span>{individualTotals[index]}원</span>
                  <span className="badge text-black">{quantities[index]}개</span>
              </div>
              </div>
            ))}
            {/* post 전송  */}
            <form className="space-y-4" onSubmit={handleSubmit}>
              <div>
                <label htmlFor="email" className="block text-sm font-medium">
                  이메일
                </label>
                <input
                  type="email"
                  id="email"
                  value={order.customer.email}
                  onChange={handleInputChange}
                  className="mt-1 block w-full rounded border-gray-300 shadow-sm focus:ring focus:ring-opacity-50 focus:ring-gray-500"
                />
              </div>
              <div>
                <label htmlFor="address" className="block text-sm font-medium">
                  주소
                </label>
                <input
                  type="text"
                  id="address"
                  value={order.customer.address}
                  onChange={handleInputChange}
                  className="mt-1 block w-full rounded border-gray-300 shadow-sm focus:ring focus:ring-opacity-50 focus:ring-gray-500"
                />
              </div>
              <div>
                <label htmlFor="postcode" className="block text-sm font-medium">
                  우편번호
                </label>
                <input
                  type="text"
                  id="postcode"
                  value={order.customer.postcode}
                  onChange={handleInputChange}
                  className="mt-1 block w-full rounded border-gray-300 shadow-sm focus:ring focus:ring-opacity-50 focus:ring-gray-500"
                />
              </div>

              <div className="text-sm text-gray-600 mt-4">
              당일 오후 2시 이후의 주문은 다음날 배송을 시작합니다.
              </div>

              <div className="flex justify-between border-t pt-4 mt-4">
                <span className="font-semibold">총금액</span>
                <span className="font-semibold">{totalPrice}원</span>
              </div>

              <button className="mt-4 w-full bg-gray-800 text-white py-2 rounded" >
                결제하기
              </button>
            </form>
            
            <button
                onClick={() => router.push("/api/login")}
                className="mt-4 w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
                >
                주문 내역 조회
            </button>
            
          </div>
        </div>
      </div>
    </div>
  );
}