import React from "react";
import { FaEnvelope, FaLinkedin, FaGithub, FaBuilding } from "react-icons/fa";
import { SiNotion } from "react-icons/si"; // Notion 아이콘 추가
import "../css/profileCard.css"

export default function ProfileCard({
  imageUrl = "https://via.placeholder.com/180",
  imageElement = null,
  name = "이름",
  title = "직책",
  affiliations = [],
  tagline = "한 줄 소개를 입력하세요",
  email,
  linkedin,
  github,
  notion,
}) {
  const affiliationText = Array.isArray(affiliations) ? affiliations.join(", ") : affiliations;

  return (
    <div className="profile-card">

      {/* 이미지 */}
      <div className="profile-image-wrapper">
        {imageElement ?? <img src={imageUrl} alt={name} className="profile-image" />}
      </div>

      {/* 이름 + 직책 */}
      <div className="profile-text">
        <p className="profile-title">{title}</p>
        <h2 className="profile-name">{name}</h2>
      </div>

      {/* 소속 */}
      <div className="profile-affiliations">
        <FaBuilding className="affiliation-icon" />
        {affiliationText}</div>

      {/* 소개 문구 */}
      <p className="profile-tagline">{tagline}</p>

      {/* 아이콘 버튼 */}
      <div className="profile-icons">
        {email && (
          <a href={`mailto:${email}`} aria-label="이메일">
            <FaEnvelope />
          </a>
        )}
        {linkedin && (
          <a href={linkedin} target="_blank" rel="noreferrer" aria-label="LinkedIn">
            <FaLinkedin />
          </a>
        )}
        {github && (
          <a href={github} target="_blank" rel="noreferrer" aria-label="GitHub">
            <FaGithub />
          </a>
        )}
        {notion && (
          <a href={notion} target="_blank" rel="noreferrer" aria-label="Notion">
            <SiNotion />
          </a>
        )}
      </div>
    </div>
  );
}
