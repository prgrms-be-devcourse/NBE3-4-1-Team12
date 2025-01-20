export interface RsData {
    resultCode: string;
    msg: string;
    data: PageDto;
}

export interface PageDto {
    currentPageNumber: number;
    pageSize: number;
    totalPages: number;
    totalItems: number;
    items:  GetResMenuOrderDto[];
}

export interface GetResMenuOrderDto {
    id: number;
    createDate: string; // LocalDateTime을 문자열로 변환
    modifyDate: string; // LocalDateTime을 문자열로 변환
    email: string;
    address: string;
    postCode: string;
    orders: GetResDetailOrderDto[];
}

export interface GetResDetailOrderDto {
    id: number;
    name: string; // Optional (Java에서는 기본값이 null 가능)
    quantity: number; // Optional (Java에서는 기본값이 null 가능)
}